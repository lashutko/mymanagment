<template>
   <div class="container">
      <div class="row">
        <div class="col-12">
          <h1>Add new programmer: </h1>
          <div class="form-login align-center">      
              <span class="success">{{success}}</span>      
              <div id="form-center" >
              <inputForms v-for="input in inputs" :props=input v-bind:input="input" v-bind:key="input" @save="saveRecord"/>
          
              </div>
              <button class="u-full-width button-primary" v-on:click="addRow" >More</button>
              <span class="error" v-for="error in errors">{{error}}</span>
              <button class="u-full-width button-primary" type="submit" v-on:click="addProgrammer" >Add programmer</button>
          </div>
       </div>
    </div>
  </div>

</template>

<script>
  import axios from 'axios';
  import Vue from 'vue';
  import inputForms from './inputForms';
import Vuex from 'vuex';

Vue.use(Vuex)
  export default {
    name: 'addprogrammer',
    components: { inputForms },
    data() {
      return {
          errors: [],
          success: '',
          i: 0,
          groups: [],
          programmers: [],
          inputs: 1
      }
    },
    methods: {
      addRow(){
        this.inputs++;

      },
      saveRecord(programmer){
        this.programmers[programmer.id-1] = programmer;
      },
      addProgrammer() {
      this.errors = [];
        let programmerTemps =[]
        for(var i = 0; i < this.programmers.length; i++){
          let programmerTemp= {name: this.programmers[i].name, email: this.programmers[i].email, tagId: this.programmers[i].tagId}; 
          programmerTemps.push(programmerTemp);
          if(!programmerTemp.name){
            this.errors.push("Name is required");
            return;
          }
          if(!programmerTemp.tagId){
            this.errors.push("Tag id is required");
            return;
          }
          if (!programmerTemp.email) {
            this.errors.push('Email required.');
            return;
          } else if (!this.validEmail(programmerTemp.email)) {
            this.errors.push('Valid email required.');
            return;
          }
          if(programmerTemps.length == this.programmers.length){
            this.saveProgrammers(programmerTemps);
          }
        }

      },
      saveProgrammers(programmerTemps){
       for(var i = 0; i < programmerTemps.length; i++){
            axios.post((this.$store.getters.getUrl + 'api/programmers'), programmerTemps[i]).then((response) =>{
              this.success = response.data['created'];
              console.log(response.data);
            })
            .catch(function(error){
              this.errors.push("Something went wrong");
            }.bind(this));
          }
      },
      validEmail(email){
        var re = /^(([^<>()\[\]\\.,;:\s@"]+(\.[^<>()\[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/;
        return re.test(email);
      }
    },
    created: function () {
      if(!localStorage.getItem('token') || localStorage.getItem('isAdmin') != 1){
        this.$router.push('/login'); 
      }
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
input{
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
.button-primary{
  border: 1px solid #c4c4ef;
  border-radius: 30px;
  background-color: transparent;
  padding: 10px 50px;
  margin-top: 20px;
  transition: 0.4s;
  outline: none;
cursor: pointer;
}
.button-primary:focus, .button-primary:active, .button-primary:hover{
  border-color: #00529c;
  color: #00529c;
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
