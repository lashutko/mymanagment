<?php

namespace App;

use Illuminate\Database\Eloquent\Model;

class ProgrammersGroup extends Model
{
    protected $fillable = ['programmer_id', 'group_id'];
    protected $primaryKey = "programmer_group_id";
}
