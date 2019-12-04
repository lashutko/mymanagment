<template>
   <div class="container">
      <div class="row">
        <div class="col-12">
          <h1>Add new project: </h1>
          <div class="form-login align-center">      
              <span class="success">{{success}}</span>             
              <input v-model="project.name" type="text" placeholder="Project name" id="name"/>
              <select v-model="project.group_id" id="groupId">    
                <option value="0" disabled selected>Chose Group</option>   
                <option v-for="group in groupsId" :value="group.id">
                  {{group.name}}
                </option>                                                   
              </select>
              <span class="error" v-for="error in errors">{{error}}</span>
              <button class="u-full-width button-primary" type="submit" v-on:click="addProject" >Add project</button>
          </div>
       </div>
    </div>
  </div>

</template>

<script>
  import axios from 'axios';
  export default {
    name: 'addProject',
    data() {
      return {
          errors: [],
          success: '',
          groupsId: [],
          tasks: [],
          task_name: '',
          project: {
            name: '',
            group_id: 0,
            curator_id: 0,
            task_id: this.$route.params.id
          }
      }
    },
    watch: {
      'project.group_id': function() {
        var groupName;
        for(var i = 0; i < this.groupsId.length; i++){
          if(this.groupsId[i].id == this.project.group_id){
            groupName = this.groupsId[i].name;
          }
        }
        this.project.name = this.task_name + "/" + groupName;
      }
    },
    methods: {
      addProject() {
        this.errors = [];
        if(!this.project.name){
          this.errors.push("Project name is required");
          return;
        }
        if(this.project.group_id===0){
          this.errors.push("Group id is required");
          return;
        }
        this.project.curator_id = localStorage.getItem('curator_id');
        axios.post(this.$store.getters.getUrl + 'api/projects', this.project).then((response) =>{
          this.success = response.data['created'];
        })
        .catch(function(error){
          this.errors.push("Something went wrong");
        }.bind(this));
      },
      getGroupsId() {
        axios.get(this.$store.getters.getUrl + 'api/groups')
         .then(function (response) {
            this.groupsId = response.data.data;
        }.bind(this))
        .catch((error)=>{
          this.$router.push('/login'); 
        });
      },
      getTaskName() {
        axios.get(this.$store.getters.getUrl + 'api/tasks')
         .then(function (response) {
            this.tasks = response.data.data;
            for(var i = 0; i < this.tasks.length; i++){
              if(this.tasks[i].task_id == this.project.task_id){
                this.task_name = this.tasks[i].name;
              }
            }
            console.log(this.task_name);
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
      this.getGroupsId();
      this.getTaskName();
    }
  }
</script>
<style scoped>
h1, h2 {
  font-size: 50px;
  text-align: center;
  width: 100%;
  margin: 30px 0;
}
.form{
  max-width: 500px;
  margin: 0 auto;
}
.error{
  color: red;
  display: block;
}
.success{
  display: block;
  margin-bottom: 30px;
  color: green;
}
#name, #tagId, #groupId{
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
select{
  text-align-last:center;
}
#groupId option{
  text-align: center;
}
 #name:focus, #tagId:focus, #groupId:focus, #name:active, #tagId:active, #groupId:active{
  border-color: #00529c;
}
ul {
  list-style-type: none;
  padding: 0;
}
li {
  display: inline-block;
  margin: 0 10px;
}
a {
  color: #42b983;
}
</style>
