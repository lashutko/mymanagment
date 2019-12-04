<?php

namespace App\Http\Resources;

use App\Programmer;
use Illuminate\Http\Resources\Json\JsonResource;

class ProgrammersGroups extends JsonResource
{
    /**
     * Transform the resource into an array.
     *
     * @param  \Illuminate\Http\Request  $request
     * @return array
     */
    public function toArray($request)
    {
        $programmerId = $this->programmer_id;
        $programmers = Programmer::all()->where('programmer_id', $programmerId)->first();
        return $programmers;
    }
}
