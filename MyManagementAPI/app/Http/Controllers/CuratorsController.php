<?php

namespace App\Http\Controllers;

use App\Curator;
use Illuminate\Http\Request;
use Illuminate\Support\Facades\Hash;
use Tymon\JWTAuth\Facades\JWTAuth;

class CuratorsController extends Controller
{
    public function login(Request $request)
    {
        // grab credentials from the request
        $credentials = $request->only('email', 'password');

        $curatorId = Curator::all()->where('email', $credentials['email'])->pluck('id');
        $curatorName = Curator::all()->where('email', $credentials['email'])->pluck('name');
        $isAdmin = Curator::all()->where('email', $credentials['email'])->pluck('isAdmin');

        try {
            // attempt to verify the credentials and create a token for the user
            if (! $token = JWTAuth::attempt($credentials)) {
                return response()->json(['error' => 'invalid_credentials'], 401);
            }
        } catch (JWTException $e) {
            // something went wrong whilst attempting to encode the token
            return response()->json(['error' => 'could_not_create_token'], 500);
        }

        // all good so return the token
        return response()->json(compact(['token', 'curatorId', 'curatorName', 'isAdmin']));
    }

    public function loginMobileApp(Request $request)
    {
        // grab credentials from the request
        $credentials = $request->only('tagId', 'mobile_token');

        $token = bcrypt($credentials['mobile_token']);

        $user=Curator::where('tagId','=', $credentials['tagId'])->first();

        if(Hash::check($credentials['mobile_token'], $user->mobile_token)) {

            //Try to add token
            if (!$userToken=JWTAuth::fromUser($user)) {
                return response()->json(['error' => 'invalid_credentials'], 401);
            }

        }
        return response()->json(compact('userToken'));

    }
}
