<?php

namespace App\Http\Resources;

use Illuminate\Http\Resources\Json\JsonResource;
use App\Project;

class Task extends JsonResource
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
            'task_id' => $this->task_id,
            'name' => $this->name,
            'curator_id' => $this->curator_id,
            'projects' => Project::all()->where('task_id', $this->task_id)->toArray()
        ];
    }
}
