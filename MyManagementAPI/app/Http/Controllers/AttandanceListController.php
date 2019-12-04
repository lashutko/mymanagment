<?php

namespace App\Http\Controllers;

use App\AttendanceList;
use App\Http\Resources\AttendanceList as AttandanceListResource;
use App\Http\Resources\ProgrammersGroups as ProgrammersGroupsResource;
use Illuminate\Http\Request;
use App\Activity;
use App\Project;
use App\ProgrammersGroup;
use Tymon\JWTAuth\Facades\JWTAuth;
use Illuminate\Foundation\Auth\User;

class AttandanceListController extends Controller
{
    /**
     * Display a listing of the resource.
     *
     * @return \Illuminate\Http\Response
     */
    public function index()
    {
        $curator = JWTAuth::parseToken()->authenticate();
        $isAdmin = $curator->isAdmin;
        if(!$isAdmin) {
            $attandanceList = AttendanceList::all();
            return AttandanceListResource::collection($attandanceList);
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
            $activity = Activity::all()->where('activity_id', $request->activity_id)->first();
            $activity->checked = true;
            $activity->save();

            AttendanceList::create($request->all());

            return response()->json([
                'created' => 'Programmer was added to attandance list'
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
            $project = Project::all()->where('project_id', $id)->first();
            $programmersGroup = ProgrammersGroup::all()->where('group_id', $project->group_id);
            $programmers = ProgrammersGroupsResource::collection($programmersGroup);


            $ids= array();
            $i = 0;
            foreach($programmers as $programmer){
                $activitiesProgrammer = AttendanceList::all()->where('programmer_id', $programmer->programmer_id)->pluck('activity_id');
                $activities = Activity::all()->where('project_id', $id)->pluck('activity_id')->toArray();
                $activitiesWithProgrammer = 0;
                for($i=0; $i<count($activitiesProgrammer); $i++){
                    if(in_array($activitiesProgrammer[$i], $activities)){
                        $activitiesWithProgrammer++;
                    }
                }
                $activities = count($activities);

                $attandanceProgrammer = $activitiesWithProgrammer/$activities;
                $ids[ $programmer->programmer_id]['programmer'] = $programmer;
                $ids[ $programmer->programmer_id]['total'] = $attandanceProgrammer;
                $i++;
            }
            return $ids;
        }
        return "unauthorized";
    }

    /**
     * Show the form for editing the specified resource.
     *
     * @param  int  $id
     * @return \Illuminate\Http\Response
     */
    public function edit($id)
    {
        //
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
        //
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
            $attandance = AttendanceList::where('programmer_id', $id);
            $attandance->delete();

            return response()->json([
                'deleted' => 'Programmer was deleted'
            ], 200);
        }
    }
}
