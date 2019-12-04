<template>
   <div class="container">
      <div class="row" v-if="tasks">
        <section class="col-12">
          <h1>Tasks</h1>
          <table class="table table-hover">
              <tr class="header_table">
                  <th>Id</th>
                  <th>Name</th>
                   <th>Edit</th>
                  <th>Delete</th>
              </tr>
              <tr v-for="task in tasks">
                  <td>{{task.task_id}}</td>
                  <td><router-link class="link" :to="'/projects/' + task.task_id">{{task.name}}</router-link></td>
                  <td><router-link class="nav-button" :to="'/task/edit/' + task.task_id">Edit task</router-link></td>
                  <td><button class="delete" v-on:click=deleteTask(task.task_id)>&#x274C;</button></td>
              </tr>
          </table>
        </section>
        <div class="col-12 navigation_buttons">
          <ul>
              <li><router-link class="nav-button" to="/tasks/add">Add Task</router-link></li>
          </ul>
       </div>
      </div>
    </div>
</template>

<script>

import axios from 'axios';

export default {
  name: 'tasks',
  data () {
    return {
      tasks: []
    }
  },
  methods: {
      getTasks(){
          axios.get(this.$store.getters.getUrl + 'api/tasks')
        .then(function (response) {
            this.tasks = response.data.data;
        }.bind(this))
        .catch((error)=>{
          this.$router.push('/login'); 
        });
      },
      deleteTask(id){
         axios.delete(this.$store.getters.getUrl + 'api/tasks/'+ id)
         .then(function (response) {
            alert(response.data['deleted']);
            this.getTasks();
        }.bind(this))
        .catch((error)=>{
          this.$router.push('/login'); 
        });
      }
  },
  created: function () {
    if(!localStorage.getItem('token') || localStorage.getItem('isAdmin') == 1){
      this.$router.push('/login'); 
    }
    this.getTasks();
  }
}
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped>

</style>
