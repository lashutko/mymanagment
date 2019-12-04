<?php

namespace App\Http\Controllers;

use Illuminate\Http\Request;

use App\Curator;
use App\Http\Resources\Curator as CuratorsResource;
use Illuminate\Foundation\Auth\User;
use Tymon\JWTAuth\Facades\JWTAuth;

class CuratorsAdminController extends Controller
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
        if($isAdmin) {
            $curator = Curator::all();
            return $curator;
        }else{
            return "unauthorized";
        }
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
        if($isAdmin) {
            $request['password'] = bcrypt($request->password);
            Curator::create($request->all());

            return response()->json([
                'created' => 'Curator was added'
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
        $curator = Curator::all()->where('id', $id);
        return CuratorsResource::collection($curator);
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
            $curator = Curator::all()->where('id', $id)->first();
            return $curator;
        }else {
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
            $curator = Curator::all()->where('id', $id)->first();
            $password = Curator::all()->where('id', $id)->pluck('password');
            $curator->name = $request->name;
            $curator->email = $request->email;
            $curator->tagId = $request->tagId;
            if ($request->password != "") {
                $curator->password = bcrypt($request->password);
            }
            $curator->save();

            return response()->json([
                'updated' => 'Curator was updated'
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
            $curator = Curator::where('id', $id);
            $curator->delete();

            return response()->json([
                'deleted' => 'Curator was deleted'
            ], 200);
        }else{
            return "unauthorized";
        }
    }
}
