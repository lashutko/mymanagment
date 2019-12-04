<template>
   <div class="container">
      <div class="row">
        <div class="col-12">
          <h1>Edit task: </h1>
          <div class="form-login align-center">      
              <span class="success">{{success}}</span>             
              <input v-model="task.name" type="text" placeholder="Task name" id="name"/>
              <span class="error" v-for="error in errors">{{error}}</span>
              <button class="u-full-width button-primary" type="submit" v-on:click="saveTask()" >Edit task</button>
          </div>
       </div>
    </div>
  </div>
</template>

<script>

import axios from 'axios';

export default {
  name: 'editTask',
  data () {
    return {
      project_id: this.$route.params.id,
      errors: [],
      success: '',
      task: {
        name: '',
        task_id: parseInt(this.$route.params.id),
        _method: "PUT"
      }
    }
  },
  methods: {
    saveTask() {
        axios.post(this.$store.getters.getUrl + 'api/tasks/' + this.task.task_id, this.task)
        .then((response) =>{
          alert(response.data['updated']);
          getTask(this.task.task_id);
        })
        .catch((error)=>{
         // this.$router.push('login'); 
        });

    },
    getTask(id) {
      axios.get(this.$store.getters.getUrl + 'api/tasks/' + this.task.task_id + '/edit')
       .then(function (response) {
          this.task.name = response.data[0].name;
        }.bind(this))
        .catch((error)=>{
          //this.$router.push('/login'); 
        });
    }
  },
  created: function () {
    if(!localStorage.getItem('token') || localStorage.getItem('isAdmin') == 1){
      this.$router.push('/login'); 
    }
    this.getTask(this.task.task_id);
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
