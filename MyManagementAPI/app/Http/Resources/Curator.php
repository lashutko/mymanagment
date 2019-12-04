<?php

namespace App\Http\Resources;

use Illuminate\Http\Resources\Json\JsonResource;
use Illuminate\Support\Str;

class Curator extends JsonResource
{
    /**
     * Transform the resource into an array.
     *
     * @param  \Illuminate\Http\Request  $request
     * @return array
     */
    public function toArray($request)
    {
        $mobile_token = Str::random();
        $curator = Curator::where('id', $this->id)->first();
        $curator->mobile_token =  bcrypt($mobile_token);
        $curator->save();
        return [
            'id' => $this->id,
            'name' => $this->name,
            'tagId' => $this->tagId,
            'mobile_token' => $mobile_token
        ];
    }
}
