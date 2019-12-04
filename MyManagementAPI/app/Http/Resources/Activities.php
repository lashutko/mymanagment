<?php

namespace App\Http\Resources;

use App\AttendanceList;
use App\Project;
use App\Activity;
use App\Curator;
use App\Programmer;
use Illuminate\Http\Resources\Json\JsonResource;
use function MongoDB\BSON\toJSON;
use phpDocumentor\Reflection\Types\Object_;
use PhpParser\Node\Expr\Array_;
use function Sodium\add;

class Activities extends JsonResource
{
    /**
     * Transform the resource into an array.
     *
     * @param  \Illuminate\Http\Request  $request
     * @return array
     */
    public function toArray($request)
    {
        $programmers_id = AttendanceList::all()->where('activity_id', $this->activity_id)->pluck('programmer_id');
        $programmers = [];
        foreach($programmers_id as $id) {
            $programmer = Programmer::all()->where('programmer_id', $id);
            $programmers[$id]['id'] = $programmer->pluck('programmer_id')->first();
            $programmers[$id]['name'] = $programmer->pluck('name')->first();
            $programmers[$id]['tagId'] = $programmer->pluck('tagId')->first();
            $dataPresence = AttendanceList::all()->where('programmer_id', $id)->pluck('data_presence')->first();
            $programmers[$id]['datePresence'] = $dataPresence;
            $programmers[$id]['groupId'] = $programmer->pluck('group_id')->first();
        }

        /*$groupId = Project::where('project_id', $this->project_id)->pluck('group_id');
        $programmers_id_All = Programmer::where('group_id', $groupId)->pluck('programmer_id');
        $programmers_All = [];
        foreach($programmers_id_All as $id) {
            $programmer = Programmer::all()->where('programmer_id', $id);
            $programmers_All[$id]['id'] = $programmer->pluck('programmer_id');
            $programmers_All[$id]['name'] = $programmer->pluck('name');
            $programmers_All[$id]['tagId'] = $programmer->pluck('tagId');
            $programmers_All[$id]['groupId'] = $programmer->pluck('group_id');
        }*/

        return [
            'id' => $this->activity_id,
            'activityDate' => $this->activityDate,
            'hour' => $this->hour,
            'title' => $this->title,
            'duration' => $this->duration,
            'recurrence_id' => $this->recurrence_id,
            'programmers' => $programmers,
            'project' => Project::all()->where('project_id', $this->project_id)->first(),
            'checked' => $this->checked
        ];
    }
}
