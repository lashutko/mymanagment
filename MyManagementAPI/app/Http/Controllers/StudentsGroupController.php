<?php

namespace App\Http\Controllers;

use App\ProgrammersGroup;
use App\Http\Resources\ProgrammersGroups as ProgrammersGroupsResource;
use Illuminate\Http\Request;
use Tymon\JWTAuth\Facades\JWTAuth;
use Illuminate\Foundation\Auth\User;

class ProgrammersGroupController extends Controller
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
            $programmersGroup = ProgrammersGroup::all();
            return ProgrammersGroupsResource::collection($programmersGroup);
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
            ProgrammersGroup::create($request->all());

            return response()->json([
                'created' => 'Programmer was added'
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
            $programmersGroup = ProgrammersGroup::all()->where('group_id', $id);
            return ProgrammersGroupsResource::collection($programmersGroup);
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
            $programmer = ProgrammersGroup::where('programmer_id', $id)->first();
            $programmer->delete();

            return response()->json([
                'deleted' => 'Programmer was deleted'
            ], 200);
        }else{
            return "unauthorized";
        }
    }
}
