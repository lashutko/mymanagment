<?php

namespace App\Http\Resources;

use App\Http\Resources\Projects as ProjectsResource;
use App\Curator;
use App\Programmer;
use App\Project;
use Illuminate\Http\Resources\Json\JsonResource;
use App\ProgrammersGroup;
use App\Http\Resources\ProgrammersGroups as ProgrammersGroupsResource;

class Groups extends JsonResource
{
    /**
     * Transform the resource into an array.
     *
     * @param  \Illuminate\Http\Request  $request
     * @return array
     */
    public function toArray($request)
    {
        $curator_id = Project::all()->where('group_id', $this->group_id)->pluck('curator_id');
        $curators = [];
        foreach($curator_id as $id) {
            $curator = Curator::all()->where('id', $id);
            $curators[$id]['name'] = $curator->pluck('name');
        }
        $programmersGroup = ProgrammersGroup::all()->where('group_id', $this->group_id);
        $programmers = ProgrammersGroupsResource::collection($programmersGroup);
        return [
            'id' => $this->group_id,
            'name' => $this->name,
            'curator' =>  $curators,
            'programmers' => $programmers
        ];
    }
}
