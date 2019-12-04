<?php

namespace App\Http\Resources;

use Illuminate\Http\Resources\Json\JsonResource;
use App\AttendanceList as AttandanceListApp;
use App\Activity;
use App\Project;
use App\ProgrammersGroup;
use App\Http\Resources\ProgrammersGroups as ProgrammersGroupsResource;

class AttendanceList extends JsonResource
{
    /**
     * Transform the resource into an array.
     *
     * @param  \Illuminate\Http\Request  $request
     * @return array
     */
    public function toArray($request)
    {
        $project_id = Activity::all()->where('activity_id', $this->activity_id)->pluck('project_id');
        $activitiesProgrammer = AttandanceListApp::all()->where('programmer_id', $this->programmer_id)->pluck('activity_id');

        $activities = Activity::all()->where('project_id', $project_id[0])->pluck('activity_id')->toArray();
        $activitiesWithProgrammer = 0;
        for($i=0; $i<count($activitiesProgrammer); $i++){
            if(in_array($activitiesProgrammer[$i], $activities)){
                $activitiesWithProgrammer++;
            }
        }
        $activities = count($activities);
        $attandanceProgrammer = $activitiesWithProgrammer/$activities;
        return [
            'attendance_lists_id ' => $this->attendance_lists_id,
            'data_presence ' => $this->data_presence,
            'programmer_id ' => $this->programmer_id,
            'activity_id ' =>  $this->activity_id,
            '$attandanceProgrammer' =>  $attandanceProgrammer
        ];
    }
}
