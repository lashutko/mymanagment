<?php

namespace App\Http\Controllers;

use Illuminate\Http\Request;
use App\Task;
use App\Project;
use App\Http\Resources\Task as TaskResource;
use App\Http\Resources\Projects as ProjectsResource;
use Illuminate\Foundation\Auth\User;
use Tymon\JWTAuth\Facades\JWTAuth;

class TasksController extends Controller
{
    /**
     * Display a listing of the resource.
     *
     * @return \Illuminate\Http\Response
     */
    public function index()
    {
        $curator = JWTAuth::parseToken()->authenticate();
        $curatorId = $curator->id;
        $isAdmin = $curator->isAdmin;
        if(!$isAdmin) {
            $tasks = Task::all()->where('curator_id', $curatorId);
            return TaskResource::collection($tasks);
        }
        return "unauthorized";
    }

    /**
     * Show the form for creating a new resource.
     *
     * @return \Illuminate\Http\Response
     */
    public function create()
    {
        //
    }

    /**
     * Store a newly created resource in storage.
     *
     * @param  \Illuminate\Http\Request  $request
     * @return \Illuminate\Http\Response
     */
    public function store(Request $request)
    {
        $curator = JWTAuth::parseToken()->authenticate();
        $isAdmin = $curator->isAdmin;
        if(!$isAdmin) {
            Task::create($request->all());

            return response()->json([
                'created' => 'Task was added'
            ], 201);
        }else{
            return "unauthorized";
        }
    }

    /**
     * Display the specified resource.
     *
     * @param  int  $id
     * @return \Illuminate\Http\Response
     */
    public function show($id)
    {
        $curator = JWTAuth::parseToken()->authenticate();
        $isAdmin = $curator->isAdmin;
        if(!$isAdmin) {
            $project = Project::all()->where('task_id', $id);
            return ProjectsResource::collection($project);
        }else{
            return "unauthorized";
        }
    }

    /**
     * Show the form for editing the specified resource.
     *
     * @param  int  $id
     * @return \Illuminate\Http\Response
     */
    public function edit($id)
    {
        $curator = JWTAuth::parseToken()->authenticate();
        $isAdmin = $curator->isAdmin;
        if(!$isAdmin) {
            $task = Task::all()->where('task_id', $id);
            return $task;
        }else{
            return "unauthorized";
        }
    }

    /**
     * Update the specified resource in storage.
     *
     * @param  \Illuminate\Http\Request  $request
     * @param  int  $id
     * @return \Illuminate\Http\Response
     */
    public function update(Request $request, $id)
    {
        $curator = JWTAuth::parseToken()->authenticate();
        $isAdmin = $curator->isAdmin;
        if(!$isAdmin) {
            $task = Task::where('task_id', $id)->first();
            $task->name = $request->name;
            $task->save();
            return response()->json([
                'updated' => 'Task was updated'
            ], 201);
        }else{
            return "unauthorized";
        }
    }

    /**
     * Remove the specified resource from storage.
     *
     * @param  int  $id
     * @return \Illuminate\Http\Response
     */
    public function destroy($id)
    {
        $curator = JWTAuth::parseToken()->authenticate();
        $isAdmin = $curator->isAdmin;
        if(!$isAdmin) {
            $task = Task::where('task_id', $id);
            $task->delete();

            return response()->json([
                'deleted' => 'Task was deleted'
            ], 200);
        }else{
            return "unauthorized";
        }
    }
}
