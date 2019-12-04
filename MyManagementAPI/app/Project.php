<?php

namespace App;

use Illuminate\Database\Eloquent\Model;

class Project extends Model
{
    protected $fillable = ['name', 'group_id', 'curator_id', 'task_id'];
    protected $primaryKey = "project_id";

}
