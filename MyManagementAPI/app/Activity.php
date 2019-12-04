<?php

namespace App;

use Illuminate\Database\Eloquent\Model;

class Activity extends Model
{
    protected $fillable = ['title', 'activityDate', 'duration', 'hour', 'project_id', 'recurrence_id', 'checked', 'curator_id', 'group_id'];
    public $primaryKey = 'activity_id';
}
