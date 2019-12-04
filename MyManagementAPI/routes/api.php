<?php

use Illuminate\Http\Request;


/*
|--------------------------------------------------------------------------
| API Routes
|--------------------------------------------------------------------------
|
| Here is where you can register API routes for your application. These
| routes are loaded by the RouteServiceProvider within a group which
| is assigned the "api" middleware group. Enjoy building your API!
|
*/

Route::middleware('auth:api')->get('/user', function (Request $request) {
    return $request->user();
});

Route::post('login', 'CuratorsController@login');
Route::post('loginMobileApp', 'CuratorsController@loginMobileApp');
Route::group([
    'middleware' => ['jwt.auth', ]

], function () {
    Route::resource('projects', 'ProjectsController');
    Route::resource('groups', 'GroupsController');
    Route::resource('activities', 'ActivitiesController');
    Route::resource('programmers', 'ProgrammersController');
    Route::resource('recurrences', 'RecurrenceController');
    Route::resource('attandancelist', 'AttandanceListController');
    Route::resource('activitiesfree', 'ActivitiesFreeController');
    Route::resource('curators', 'CuratorsAdminController');
    Route::resource('tasks', 'TasksController');
    Route::resource('programmersgroup', 'ProgrammersGroupController');
});



