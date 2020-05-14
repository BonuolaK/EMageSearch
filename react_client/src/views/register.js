import React, {Component} from "react";
import * as _appConsts from "../helpers/consts";
import SimpleReactValidator from "simple-react-validator";
import { useHistory } from "react-router-dom";

class RegisterPage extends Component {
    constructor(props) {
        super(props);
        this.state = {
            form: {
                name:'',
                domain_type:'HTTP',
                domain_url:'',
                email:'',
                password:'',
                product_types:[],
                response_type:'REDIRECT',
                response_url:''
            },
            isDomainVerified: false
        };
      //  const history = useHistory();
        this.submitForm = this.submitForm.bind(this);
        this.validator = new SimpleReactValidator({autoForceUpdate: this,
            validators: {
                shortUrl: {
                    message: 'Must be a url',
                    rule: (val, params, validator) => {
                        return validator.helpers.testRegex(val,/[-a-zA-Z0-9@:%._\+~#=]{1,256}\.[a-zA-Z0-9()]{1,6}\b([-a-zA-Z0-9()@:%_\+.~#?&//=]*)?/gi)
                    }
                }
            }
        });
        this.updateForm = this.updateForm.bind(this);
        this.verifyDomain = this.verifyDomain.bind(this);
    }


    submitForm(event){
        let _submitButton = event.target;

        let _page = this;
        if (this.validator.allValid()) {
            if(this.state.isDomainVerified){
                console.log(this.state.form);
                _submitButton.disabled = true;
                _submitButton.innerHTML = "Processing...";

                let data ={
                    name : this.state.form.name,
                    domainType : this.state.form.domain_type,
                    domainURL : this.state.form.domain_url,
                    email : this.state.form.email,
                    password : this.state.form.password,
                    responseType : this.state.form.response_type,
                    responseUrl : this.state.form.response_url,
                    productTypes : this.state.form.product_types,
                };

                fetch(`${_appConsts.API_URL}client/register`, {
                    method: 'post',
                    body: JSON.stringify(data) ,
                    headers: {
                        'Accept': 'application/json',
                        'Content-Type': 'application/json;charset=UTF-8'
                    },
                })
                    .then(res => res.json())
                    .then((data) => {
                        if(!data.hasError){
                            _submitButton.innerHTML = "Redirecting you to login page";
                            this.props.history.push('/login');
                        }
                        else{
                            _submitButton.innerHTML = "Submit";
                            _submitButton.disabled = false;
                        }


                    console.log(data);
                }).catch(function (err) {
                    alert("There was an error!");
                    _submitButton.innerHTML = "Submit";
                    _submitButton.disabled = false;
                });
            }
            else{
                alert("Please verify domain ownership");
            }


        } else {
            this.validator.showMessages();
        }
    }

    verifyDomain(event){
        let _button = event.target;
        _button.disabled = true;
        _button.innerHTML = "Verifying...";
        let _page = this;

        let shopUrl = `${this.state.form.domain_type === "HTTP"? "http://" : "https://"}${this.state.form.domain_url}`;

        if(this.validator.helpers.testRegex(this.state.form.domain_url,/[-a-zA-Z0-9@:%._\+~#=]{1,256}\.[a-zA-Z0-9()]{1,6}\b([-a-zA-Z0-9()@:%_\+.~#?&//=]*)?/gi)){
            fetch(`${_appConsts.API_URL}client/domain/verify?url=${shopUrl}`)
                .then(res => res.text())
                .then((data) => {
                    if(data == "Found"){
                        _button.innerHTML = "Verified";
                        _page.state.isDomainVerified = true;
                    }

                    else{
                        _button.disabled = false;
                        _button.innerHTML = "Verify Domain";
                        alert('Unable to verify, please check that you have put a file name verify.txt in the root folder of your domain and try again');
                    }
                })
                .catch((error) => {
                    console.log(error);
                    _button.disabled = false;
                    _button.innerHTML = "Verify Domain";
                    alert('Error: Unable to verify, please check your shop url and try again');
                })
        }
        else{
          alert("Invalid domain");
            _button.disabled = false;
            _button.innerHTML = "Verify Domain";

        }
    }


    updateForm(event){
        let shop = this.state.form;
        if(event.target.name != "product_types")
            shop[event.target.name] = event.target.value;
        else{
            let index = shop.product_types.indexOf(event.target.value.toString());
            if(index >= 0)
                shop.product_types.splice(index,1);
            else
                shop.product_types.push(event.target.value);
        }
        this.setState({shop});
    }


    render() {
        return (
            <div>
                <div className="banner-top">
                    <div className="container">
                        <h3>Register</h3>
                        <h4><a href="/home">Home</a><label>/</label>Register</h4>
                        <div className="clearfix"></div>
                    </div>
                </div>
                <div className="container">
                    <div className="row register-form">
                        <div className="col-sm-6">
                            <div>
                                <div className="form-group">
                                    <label htmlFor="name">Shop Name</label>
                                    {this.validator.message('name', this.state.form.name, 'required')}
                                    <input onChange={this.updateForm}  value={this.state.form.name}  name="name" type="text" className="form-control"/>

                                </div>

                                <div className="form-group">
                                    <div className="row">
                                        <div className="col-sm-8">
                                            {this.validator.message('domain_url', this.state.form.domain_url, 'required|shortUrl')}
                                            <div className="input-group">
                                                <div className="input-group-btn wid_30">
                                                    <select className="form-control wid_100" name="domain_type" value={this.state.form.domain_type} onChange={this.updateForm}>
                                                        <option value="HTTP">http://</option>
                                                        <option value="HTTPS">https://</option>
                                                    </select>
                                                </div>
                                                <input type="text" onChange={this.updateForm}  value={this.state.form.domain_url} className="form-control" name="domain_url" />
                                            </div>

                                        </div>
                                        <div className="col-sm-4">
                                          <button title="Place a file named verify.txt in your root domain and click the verify button
                                            to confirm ownership of this domain" className="btn btn-warning" onClick={this.verifyDomain} disabled = {this.state.isDomainVerified}>Verify Domain</button>
                                            </div>
                                    </div>

                                </div>

                                <div className="form-group">
                                    <label htmlFor="email">Email</label>
                                    {this.validator.message('email', this.state.form.email, 'required|email')}
                                    <input name="email" type="email" onChange={this.updateForm}  value={this.state.form.email} className="form-control"/>
                                </div>

                                <div className="form-group">
                                    <label htmlFor="password">Password</label>
                                    {this.validator.message('password', this.state.form.password, 'required')}
                                    <input name="password" type="password" onChange={this.updateForm} value={this.state.form.password} className="form-control"/>
                                </div>

                                <div className="form-group">
                                    <label htmlFor="password">Product Type</label>
                                    {this.validator.message('product_types', this.state.form.product_types, 'required')}
                                    <select className="form-control" name="product_types" value={this.state.form.product_types} onChange={this.updateForm} multiple>
                                        <option value="TSHIRT">T-shirt</option>
                                        <option value="CORPORATE_SHIRT">Corporate shirt</option>
                                        <option value="SWEATSHIRT">Sweatshirt</option>
                                        <option value="SUIT">Suit</option>
                                        <option value="JACKET">Jacket</option>
                                    </select>
                                </div>

                                <div className="form-group">
                                    <label htmlFor="response_type">Response Type</label>
                                    {this.validator.message('response_type', this.state.form.response_type, 'required')}
                                    <select className="form-control" name="response_type" value={this.state.form.response_type} onChange={this.updateForm}>
                                        <option value="REDIRECT">Redirect To URL</option>
                                        <option value="CALL_BACK">Call back function</option>
                                    </select>
                                </div>


                                {this.state.form.response_type === "CALL_BACK" && (
                                    <div className="form-group">
                                        <label className="text-panel"><b>Provide call back function after request. <br/><br/>Sample JSON Response  {"{type: 'sweatshirt',color: 'black', tag:'nike'}"} </b></label>
                                    </div>
                                )}
                                    {this.state.form.response_type === "REDIRECT" && (

                                    <div className="form-group">
                                        <label className="text-panel">(Allowed Tags: <b>{"{{PRODUCT_TYPE}}"}</b>, <b>{"{{PRODUCT_COLOR}}"}</b>, <b>{"{{PRODUCT_TAG}}"}</b> <br/> <br/>
                                            Sample Url: <b>http://www.abc.com?type={"{{PRODUCT_TYPE}}"}&color={"{{PRODUCT_COLOR}}"}&tag={"{{PRODUCT_TAG}}"}</b>)</label>
                                        <label htmlFor="password">Response URL </label>
                                        <div className="input-group wid_100">
                                            <div className="input-group-btn wid_30">
                                                <ul className="form-control wid_100 list-unstyled">
                                                    <li>{`${this.state.form.domain_type === "HTTP"? "http://" : "https://"}${this.state.form.domain_url}`}?</li>
                                                </ul>
                                            </div>
                                            <input type="text"  value={this.state.form.response_url} onChange={this.updateForm} className="form-control" name="response_url" />
                                        </div>
                                    </div>
                                )}

                                <div className="form-group">
                                    <button onClick={this.submitForm} className="btn btn-primary">Submit</button>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>

            </div>
        );
    };
}
export default RegisterPage



