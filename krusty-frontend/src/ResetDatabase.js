import React, { Component } from 'react';
import axios from 'axios';
import Button from '@material-ui/core/Button';
import Dialog from '@material-ui/core/Dialog';
import DialogActions from '@material-ui/core/DialogActions';
import DialogContent from '@material-ui/core/DialogContent';
import DialogContentText from '@material-ui/core/DialogContentText';
import DialogTitle from '@material-ui/core/DialogTitle';

class ResetDatabase extends Component {
  state = {
    open: false
  }

  openDialog = () => {
    this.setState({open: true})
  }

  closeDialog = () => {
    this.setState({open: false})
  }

  resetDatabase = () => {
    this.closeDialog();
    axios.post('reset', {})
      .then(response => {
        console.log(response);
        this.props.refresh();
        this.props.setStatus("The database was restored");
      })
      .catch(error => {
        alert('An error occured. See console.');
        console.log(error);
      });
  }

  render() {
    return (
      <p>
        <Button variant="contained" onClick={this.openDialog}>
          Reset database
        </Button>
        <Dialog
          open={this.state.open}
          onClose={this.closeDialog}
          aria-labelledby="alert-dialog-title"
          aria-describedby="alert-dialog-description">
          <DialogTitle id="alert-dialog-title">{"Reset database?"}</DialogTitle>
          <DialogContent>
            <DialogContentText id="alert-dialog-description">
              Do you want do reset the database?
            </DialogContentText>
          </DialogContent>
          <DialogActions>
            <Button onClick={this.resetDatabase} color="primary">
              Yes
            </Button>
            <Button onClick={this.closeDialog} color="primary" autoFocus>
              No
            </Button>
          </DialogActions>
        </Dialog>
      </p>
    );
  }
}

export default ResetDatabase;
