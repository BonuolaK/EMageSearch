import React, { Component } from 'react';
import { BrowserRouter as Router, Route, Redirect, Switch } from 'react-router-dom';

/** Layouts **/
import PublicLayoutRoute from "./layout/public/public_layout";

/** Components **/
import HomePage from "./views/home";
import RegisterPage from "./views/register";
import LoginPage from "./views/login";
import ClientPage from "./views/client";

/*
   App
 */
class App extends Component {
  render() {
    return (
        <Router>
          <Switch>
            <Route exact path="/">
              <Redirect to="/register" />
            </Route>
            <PublicLayoutRoute path="/home" component={HomePage} />
              <PublicLayoutRoute path="/register" component={RegisterPage} />
              <PublicLayoutRoute path="/login" component={LoginPage} />
              <PublicLayoutRoute path="/client" component={ClientPage} />
          </Switch>
        </Router>
    );
  }
}

export default App;