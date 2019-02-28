import React from 'react';
import styles from './Header.module.css';
import { Button } from 'reactstrap';

const header = props =>
    (<header>
        <div className={styles.HeadContainer}>
            <h3 className={styles.HeadH3}>facebook</h3>
            <Button color="success">Sign up</Button>
        </div>
    </header>);

export default header;