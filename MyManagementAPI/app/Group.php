<?php

namespace App;

use Illuminate\Database\Eloquent\Model;

class Group extends Model
{
    protected $fillable = ['name', 'curator_id'];
    protected $primaryKey = "group_id";
}
