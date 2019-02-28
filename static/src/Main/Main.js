import React, { Component } from 'react';
import { Button, Input } from 'reactstrap';
import styles from './Main.module.css';

class Main extends Component {
    render() {
        return (
            <div className={styles.LoginContainer}>
                <form>
                    <span>Log into Facebook</span>
                    <Input placeholder="Email" type="text" />
                    <Input placeholder="Password" type="password" />
                    <Button color="primary">Log in</Button>
                    <span>or</span>
                    <Button color="success">Create New Account</Button>
                </form>
            </div>
        )
    }
}

export default Main;

