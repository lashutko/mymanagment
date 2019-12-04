<?php

use Illuminate\Support\Facades\Schema;
use Illuminate\Database\Schema\Blueprint;
use Illuminate\Database\Migrations\Migration;

class AlterTableProject extends Migration
{
    /**
     * Run the migrations.
     *
     * @return void
     */
    public function up()
    {
        Schema::table('projects', function (Blueprint $table) {
            $table->integer('group_id')
                ->unsigned()
                ->nullable();
            $table->integer('curator_id')
                ->unsigned()
                ->nullable();
            $table->integer('task_id')
                ->unsigned()
                ->nullable();
            $table->foreign('group_id')
                ->references('group_id')
                ->on('groups');
            $table->foreign('curator_id')
                ->references('id')
                ->on('curators');
            $table->foreign('task_id')
                ->references('task_id')
                ->on('tasks');
        });
    }

    /**
     * Reverse the migrations.
     *
     * @return void
     */
    public function down()
    {
        //
    }
}
