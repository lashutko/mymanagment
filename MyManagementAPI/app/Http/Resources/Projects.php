<?php

namespace App\Http\Resources;

use App\Activity;
use App\Curator;
use Illuminate\Http\Resources\Json\JsonResource;

use App\Group;

class Projects extends JsonResource
{
    /**
     * Transform the resource into an array.
     *
     * @param  \Illuminate\Http\Request  $request
     * @return array
     */
    public function toArray($request)
    {
        return [
            'id' => $this->project_id,
            'name' => $this->name,
            'group' => Group::all()->where('group_id', $this->group_id)->toArray(),
            'curator' => Curator::all()->where('id', $this->curator_id)->toArray(),
            'activities' => Activity::all()->where('project_id', $this->project_id)->toArray(),
        ];
    }
}
