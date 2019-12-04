<template>
   <div class="container">
      <div class="row">
        <div class="col-12">
          <h1>Edit curator: </h1>
          <div class="form-login align-center">      
              <span class="success">{{success}}</span>             
              <input v-model="curator.name" type="text" placeholder="Curator name and surename" id="name"/>
              <input v-model="curator.password" type="text" placeholder="Curator password" id="password"/>
              <input v-model="curator.email" type="email" placeholder="Curator email" id="email"/>
              <input v-model="curator.tagId" type="text" placeholder="Curator tag id" id="tagId"/>
              <span class="error" v-for="error in errors">{{error}}</span>
              <button class="u-full-width button-primary" type="submit" v-on:click="saveCurator()" >Edit curator</button>
          </div>
       </div>
    </div>
  </div>

</template>

<script>
  import axios from 'axios';
  export default {
    name: 'editCurator',
    data() {
      return {
          errors: [],
          success: '',
          curator_id: this.$route.params.id,
          curator: {
            name: '',
            password: '',
            email: '',
            tagId: '',
            _method: "PUT"
          }
      }
    },
    methods: {
      saveCurator() {
        if(!curator.name){
          this.errors.push("Name is required");
          return;
        }
        if(!curator.tagId){
          this.errors.push("Tag id is required");
          return;
        }
        if(!curator.password){
          this.errors.push("Password is required");
          return;
        }
        if (!curator.email) {
          this.errors.push('Email required.');
          return;
        } else if (!this.validEmail(curator.email)) {
          this.errors.push('Valid email required.');
          return;
        }
        console.log(this.curator);
        axios.post(this.$store.getters.getUrl + 'api/curators/' + this.curator_id, this.curator)
        .then((response) =>{
          alert(response.data['updated']);
          this.getCurator(this.curator_id);
        })
        .catch((error)=>{
          this.$router.push('/login'); 
        });
      },
      getCurator(id) {
        axios.get(this.$store.getters.getUrl + 'api/curators/' + id + '/edit')
         .then(function (response) {
            this.curator.name = response.data.name;
            this.curator.email = response.data.email;
            this.curator.tagId = response.data.tagId;
            console.log(response.data[0]);
          }.bind(this))
          .catch((error)=>{
            this.$router.push('/login'); 
          });
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
      this.getCurator(this.curator_id);
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
