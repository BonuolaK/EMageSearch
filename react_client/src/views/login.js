import React, {Component} from "react";
import * as _appConsts from "../helpers/consts";
import SimpleReactValidator from "simple-react-validator";

class LoginPage extends Component {
    constructor(props) {
        super(props);

        this.state = {
            form: {
                email:'',
                password:''
            }
        };

        this.submitForm = this.submitForm.bind(this);
        this.validator = new SimpleReactValidator({autoForceUpdate: this,
            validators: {
                fileExt: {
                    message: 'Must be a valid image',
                    rule: (val, params, validator) => {
                        return validator.helpers.testRegex(val.name,/(.jpg|.jpeg|.png)$/i)
                    }
                },
                shortUrl: {
                    message: 'Must be a url',
                    rule: (val, params, validator) => {
                        return validator.helpers.testRegex(val,/[-a-zA-Z0-9@:%._\+~#=]{1,256}\.[a-zA-Z0-9()]{1,6}\b([-a-zA-Z0-9()@:%_\+.~#?&//=]*)?/gi)
                    }
                }
            }
        });
        this.updateForm = this.updateForm.bind(this);
    }

    submitForm(event){
        let _submitButton = event.target;

        _submitButton.disabled = true;
        _submitButton.innerHTML = "Processing...";

        let _page = this;
        if (this.validator.allValid()) {

                let data ={
                    email : this.state.form.email,
                    password : this.state.form.password
                };

                fetch(`${_appConsts.API_URL}login`, {
                    method: 'post',
                    body: JSON.stringify(data)})
                        .then(res => res)
                        .then((data) => {

                            _submitButton.innerHTML = "Redirecting you to your home page";
                            // auth header
                           let auth =  data.headers.get('Authorization')
                           let currentUser = JSON.parse(atob(auth.split('.')[1]));
                           console.log(currentUser);
                            currentUser.token = auth;
                           localStorage.setItem(_appConsts.CURRENT_USER, JSON.stringify(currentUser));
                            this.props.history.push('/client');

                    }).catch(function (err) {
                    alert("There was an error!");
                    _submitButton.innerHTML = "Submit";
                    _submitButton.disabled = false;
                });



        } else {
            this.validator.showMessages();
        }
    }

    updateForm(event){
        let login = this.state.form;
        login[event.target.name] = event.target.value;
        this.setState({login});
    }


    render() {
        return (
            <div>
                <div className="banner-top">
                    <div className="container">
                        <h3>Login</h3>
                        <h4><a href="/home">Home</a><label>/</label>Login</h4>
                        <div className="clearfix"></div>
                    </div>
                </div>


                <div className="login">

                    <div className="main-agileits">
                        <div className="form-w3agile">
                            <h3>Login</h3>
                            <form action="#" method="post">
                                <div className="form-group">
                                    <label for="email">Email</label>
                                    {this.validator.message('email', this.state.form.email, 'required|email')}
                                    <input onChange={this.updateForm}  className="form-control"  value={this.state.form.email}  type="text"  name="email" />
                                        <div className="clearfix"></div>
                                </div>
                                <div className="form-group">
                                    <label htmlFor="password">Password</label>
                                    {this.validator.message('password', this.state.form.password, 'required')}
                                    <input onChange={this.updateForm}  className="form-control" value={this.state.form.password}  type="password" name="password" />
                                    <div className="clearfix"></div>
                                </div>

                                <button onClick={this.submitForm} className="btn btn-primary">Submit</button>
                            </form>
                        </div>

                    </div>
                </div>
            </div>
        );
    };
}
export default LoginPage



