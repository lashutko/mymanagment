<?php

use Illuminate\Support\Facades\Schema;
use Illuminate\Database\Schema\Blueprint;
use Illuminate\Database\Migrations\Migration;

class AlterTableActivity extends Migration
{
    /**
     * Run the migrations.
     *
     * @return void
     */
    public function up()
    {
        Schema::table('activities', function (Blueprint $table) {
            $table->integer('project_id')
                ->unsigned()
                ->nullable();
            $table->integer('recurrence_id')
                ->unsigned()
                ->nullable();
            $table->integer('curator_id')
                ->unsigned()
                ->nullable();
            $table->integer('group_id')
                ->unsigned()
                ->nullable();
            $table->foreign('project_id')
                ->references('project_id')
                ->on('projects');
            $table->foreign('recurrence_id')
                ->references('recurrence_id')
                ->on('recurrences');
            $table->foreign('curator_id')
                ->references('id')
                ->on('curators');
            $table->foreign('group_id')
                ->references('group_id')
                ->on('groups');
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
