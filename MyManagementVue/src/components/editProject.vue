<template>
   <div class="container">
      <div class="row">
        <div class="col-12">
          <h1>Edit project: </h1>
          <div class="form-login align-center">      
              <span class="success">{{success}}</span>             
              <input v-model="project.name" type="text" placeholder="Project name" id="name"/>
              <select v-model="project.group_id" id="groupId">    
                <option value="0" disabled selected>Chose Group</option>   
                <option v-for="group in groups" :value="group.id">
                  {{group.name}}
                </option>                                                   
              </select>
              <select v-model="project.task_id" id="groupId">    
                <option value="0" disabled selected>Chose Task</option>   
                <option v-for="task in tasks" :value="task.task_id">
                  {{task.name}}
                </option>                                                   
              </select>
              <span class="error" v-for="error in errors">{{error}}</span>
              <button class="u-full-width button-primary" type="submit" v-on:click="saveProject()" >Edit project</button>
          </div>
       </div>
    </div>
  </div>
</template>

<script>

import axios from 'axios';

export default {
  name: 'editProject',
  data () {
    return {
      project_id: this.$route.params.id,
      errors: [],
      success: '',
      groups: [],
      tasks: [],
      project: {
        name: '',
        group_id: 0,
        curator_id: 0,
        task_id: parseInt(this.$route.params.id),
        _method: "PUT"
      }
    }
  },
  methods: {
    saveProject() {
        console.log(this.project);
        axios.post(this.$store.getters.getUrl + 'api/projects/' + this.project_id, this.project)
        .then((response) =>{
          alert(response.data['updated']);
          getProject(this.project_id);
        })
        .catch((error)=>{
          this.$router.push('login'); 
        });

    },
    getProject(id) {
      axios.get(this.$store.getters.getUrl + 'api/projects/' + id + '/edit')
       .then(function (response) {
          this.project.name = response.data.name;
          this.project.group_id = response.data.group_id;
          this.project.curator_id = response.data.curator_id;
        }.bind(this))
        .catch((error)=>{
          //this.$router.push('/login'); 
        });
    },
    getGroups(){
      axios.get(this.$store.getters.getUrl + 'api/groups')
        .then(function (response) {
          this.groups = response.data.data;
          console.log(this.groups);
      }.bind(this))
      .catch((error)=>{
        //this.$router.push('/login'); 
      });
    },
    getTasks(){
          axios.get(this.$store.getters.getUrl + 'api/tasks')
        .then(function (response) {
            this.tasks = response.data.data;
            console.log(this.tasks)
        }.bind(this))
        .catch((error)=>{
          //this.$router.push('/login'); 
        });
      },
  },
  created: function () {
    if(!localStorage.getItem('token') || localStorage.getItem('isAdmin') == 1){
      this.$router.push('/login'); 
    }
    this.getProject(18);
    this.getGroups();
    this.getTasks();
  }
}
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped>
h3,h4{
  text-align: left;
}
ul {
  list-style-type: none;
  padding: 0;
}
li {
  display: inline-block;
  margin: 0 10px;
}
input, select{
  width: 100%;
  max-width: 400px;
  display: block;
  margin: 0 auto;
  padding: 10px;
  border: 1px solid #c4c4ef;
  border-radius: 30px;
  text-align: center;
  margin-bottom: 15px;
  outline: none;
}
 #name:focus, #name:active{
  border-color: #00529c;
}
a {
  color: #42b983;
}
</style>
