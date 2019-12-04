<?php

namespace App\Http\Controllers;

use App\Project;
use App\Http\Resources\Projects;
use App\Http\Resources\Projects as ProjectsResource;
use Illuminate\Foundation\Auth\User;
use Illuminate\Http\Request;
use Tymon\JWTAuth\Facades\JWTAuth;

class ProjectsController extends Controller
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
            $projects = Project::all()->where('curator_id', $curatorId);
            return ProjectsResource::collection($projects);
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
            Project::create($request->all());

            return response()->json([
                'created' => 'Project was added'
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
            $project = Project::all()->where('project_id', $id);
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
            $project = Project::all()->where('project_id', $id)->first();
            return $project;
        }else{
            return "unauthorized";
        }
    }

    /**
     * Update the specified resource in storage.
     *
     * @param  \Illuminate\Http\Request  $request
     * @param  int $id
     * @return \Illuminate\Http\Response
     */
    public function update(Request $request, $id)
    {
        $curator = JWTAuth::parseToken()->authenticate();
        $isAdmin = $curator->isAdmin;
        if(!$isAdmin) {
            $project = Project::where('project_id', $id)->first();
            $project->name = $request->name;
            $project->group_id = $request->group_id;
            $project->task_id = $request->task_id;
            $project->save();

            return response()->json([
                'updated' => 'Project was updated'
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
            $project = Project::where('project_id', $id);
            $project->delete();

            return response()->json([
                'deleted' => 'Project was deleted'
            ], 200);
        }else{
            return "unauthorized";
        }
    }
}
