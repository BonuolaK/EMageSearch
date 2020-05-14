import React, {Component} from "react";
import * as _appConsts from "../helpers/consts";
import SimpleReactValidator from "simple-react-validator";

class ClientPage extends Component {
    constructor(props) {
        super(props);

        this.state = {
            code:'',
            client:''
        };

    }

    componentDidMount() {
        let curUser = localStorage.getItem(_appConsts.CURRENT_USER);
        let user = JSON.parse(curUser);

        fetch(`${_appConsts.API_URL}client/${user.clientId}/code`,{
            headers: {
                'Content-Type': 'application/json;charset=UTF-8',
                'Authorization': 'Bearer ' + user.token
            }
        })
            .then(res => res.json())
            .then((data) => {
                console.log(data);
                this.setState({"client":JSON.stringify(data.data.client)});

                this.setState({"code":data.data.code});
            })
            .catch((error) => {

            })
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
                            <h3>YOUR DETAILS</h3>
                            <div>{this.state.client}></div>

                            <h3>CODE</h3>
                            <div>{this.state.code}></div>


                        </div>

                    </div>
                </div>
            </div>
        );
    };
}
export default ClientPage



