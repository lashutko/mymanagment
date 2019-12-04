<?php

namespace App;

use Illuminate\Database\Eloquent\Model;

class AttendanceList extends Model
{
    protected $fillable = ['data_presence', 'programmer_id', 'activity_id'];
}
