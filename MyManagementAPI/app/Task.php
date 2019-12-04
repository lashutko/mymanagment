<?php

namespace App;

use Illuminate\Database\Eloquent\Model;

class Task extends Model
{
    protected $fillable = ['name', 'curator_id'];
    protected $primaryKey = "task_id";
}
