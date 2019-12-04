<template>
   <div class="container">
      <div class="row">
        <div class="col-12">
          <h1>Add new programmer: </h1>
          <div class="form-login align-center">      
              <span class="success">{{success}}</span>             
              <input v-model="search" type="search" placeholder="Programmer email" id="name" autocomplete="off" />
              <div class="input_prompt" v-if="!searched">
                  <ul>
                    <li v-for="(programmer, index) in filteredList" v-if="index<4" v-on:click="selectProgrammer(programmer.programmer_id)">
                      <span>{{programmer.email}}</span>
                    </li>
                  </ul>
              </div>
              <select v-model="programmer.group_id" id="groupId">    
                <option value="0" disabled selected>Chose Group</option>   
                <option v-for="group in groups" :value="group.id">
                  {{group.name}}
                </option>                                                   
              </select>
              <span class="error" v-for="error in errors">{{error}}</span>
              <button class="u-full-width button-primary" type="submit" v-on:click="addProgrammer()" >Add programmer</button>
          </div>
       </div>
    </div>
  </div>

</template>

<script>
  import axios from 'axios';
  export default {
    name: 'addProgrammerGroup',
    data() {
      return {
          errors: [],
          success: '',
          groups: [],
          search: "",
          searched: 0,
          programmers: [],
          programmer: {
            programmer_id: null,
            group_id: 0
          }
      }
    },
    computed: {
      filteredList() {
        
        return this.programmers.filter(programmer => {
          return programmer.email.toLowerCase().includes(this.search.toLowerCase());
        })
      }
    },
    watch: {
      'search': function() {
        if(this.search===""){
          this.searched = 0;
        }
      }
    },
    methods: {
      selectProgrammer(programmer_id){
          this.programmer.programmer_id = programmer_id;
          for(var i = 0; i < this.programmers.length; i++){
            if(this.programmer.programmer_id === this.programmers[i].programmer_id){
              this.search = this.programmers[i].email;
              this.searched = 1;
            }
          }
      },
      addProgrammer() {
        this.errors = [];
        console.log(this.programmer);
        if(!this.programmer.programmer_id){
          this.errors.push("Email is required");
          return;
        }
        if(this.programmer.group_id === 0){
          this.errors.push("Group is required");
          return;
        }
       axios.post((this.$store.getters.getUrl + 'api/programmersgroup'), this.programmer).then((response) =>{
          this.success = response.data['created'];
        })
        .catch(function(error){
          this.errors.push("Something went wrong");
        }.bind(this));
      },
      getProgrammers() {
        axios.get(this.$store.getters.getUrl + 'api/programmers')
         .then(function (response) {
            this.programmers = response.data;
            console.log(response.data);
        }.bind(this))
        .catch((error)=>{
          this.$router.push('/login'); 
        });
      },
      getGroupsId() {
        axios.get(this.$store.getters.getUrl + 'api/groups')
         .then(function (response) {
            this.groups = response.data.data;
            console.log(response.data.data);
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
      this.getProgrammers();
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
.input_prompt{
  text-align: center;
  position: absolute;
  width: 100%;
  z-index: 99999;
  background-color: #fff;
}
.input_prompt ul{
  margin: 0;
}
.input_prompt li{
  width: 100%;
  margin: 0;
}
.input_prompt li span{
  max-width: 300px;
  display: table;
  margin: 0 auto;
  text-align: center;
}
.input_prompt li:hover span{
  background-color: #f6f7e8;
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
