import React , { Component } from "react";
import { Route } from 'react-router-dom';
import './css/style.css';

const PublicLayout = ({children}) => {
    return (
        <div>
            <div className="header">
                <div className="container">
                    <div className="logo">
                        <h1><a href="/home">Emage Search<span></span></a></h1>
                    </div>
                    <div className="head-t">
                        <ul className="card">
                            <li><a href="/login"><i className="fa fa-user" aria-hidden="true"></i>Login</a></li>
                            <li><a href="/register"><i className="fa fa-arrow-right" aria-hidden="true"></i>Register</a>
                            </li>
                        </ul>
                    </div>
                </div>
            </div>
            <div>
                {children}
            </div>

            <div className="footer">
                <div className="container">

                    <div className="copy-right">
                        <p> &copy; 2019 EMAGE SEARCH</p>
                    </div>
                </div>
            </div>
        </div>
    )};


const PublicLayoutRoute = ({component: Component, ...rest}) => {
    return (
        <Route {...rest} render={matchProps => (
            <PublicLayout>
                <Component {...matchProps} />
            </PublicLayout>
        )} />
    )
};

export default PublicLayoutRoute;




