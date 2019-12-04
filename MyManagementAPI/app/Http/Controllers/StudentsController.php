<?php

namespace App\Http\Controllers;

use App\Programmer;
use Illuminate\Http\Request;
use Illuminate\Foundation\Auth\User;
use Tymon\JWTAuth\Facades\JWTAuth;

class ProgrammersController extends Controller
{
    /**
     * Display a listing of the resource.
     *
     * @return \Illuminate\Http\Response
     */
    public function index()
    {
        return Programmer::all();
    }

    /**
     * Show the form for creating a new resource.
     *
     * @return \Illuminate\Http\Response
     */
    public function create()
    {

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
        if($isAdmin) {
            Programmer::create($request->all());

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
        if($isAdmin) {
            return Programmer::all()->where('programmer_id', $id)->first();
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
        if($isAdmin) {
            $programmer = Programmer::all()->where('programmer_id', $id);
            return $programmer;
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
        if($isAdmin) {
            $programmer = Programmer::all()->where('programmer_id', $id)->first();
            $programmer->name = $request->name;
            $programmer->email = $request->email;
            $programmer->tagId = $request->tagId;
            $programmer->save();

            return response()->json([
                'updated' => 'Programmer was updated'
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
        if($isAdmin) {
            $programmer = Programmer::where('programmer_id', $id);
            $programmer->delete();

            return response()->json([
                'deleted' => 'Programmer was deleted'
            ], 200);
        }else{
            return "unauthorized";        }
    }
}
