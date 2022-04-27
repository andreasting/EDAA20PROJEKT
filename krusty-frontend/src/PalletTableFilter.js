import React, { Component } from 'react';

import TextField from '@material-ui/core/TextField';
import Button from '@material-ui/core/Button';
import Grid from '@material-ui/core/Grid';

const initialState = () => ({
  cookie: "",
  from: "",
  to: "",
  blocked: ""
})

class PalletTableFilter extends Component {
  state = initialState()

  handleInputChange = (e) => {
    this.setState({
      [e.target.name]: e.target.value
    });
  }

  handleSubmit = () => {
    this.props.palletsFilter(this.state);
  }

  handleClear = () => {
    this.setState(initialState());
    this.props.palletsFilter({ });
  }

  render() {
    return (
      <form style={{paddingLeft: "10px", marginBottom: "10px", marginTop:"5px"}}>
        <Grid
          container
          direction="row"
          justify="flex-start"
          alignItems="flex-end">
          <TextField
            style={{minWidth: "140px"}}
            id="cookie"
            select
            label="Select Cookie"
            value={this.state.cookie}
            onChange={this.handleInputChange}
            name="cookie"
            SelectProps={{
              native: true
            }}>
            <option value=""></option>
            {this.props.cookies.map(c =>
              <option key={c.name} value={c.name}>{c.name}</option>
            )}
          </TextField>
          <TextField
            style={{marginLeft: "10px"}}
            id="from"
            label="From"
            type="date"
            value={this.state.from}
            onChange={this.handleInputChange}
            name="from"
            InputLabelProps={{
              shrink: true,
            }}
          />
          <TextField
            style={{marginLeft: "10px"}}
            id="to"
            label="To"
            type="date"
            value={this.state.to}
            onChange={this.handleInputChange}
            name="to"
            InputLabelProps={{
              shrink: true,
            }}
          />
          <TextField
            style={{marginLeft: "10px", minWidth: "90px"}}
            id="blocked"
            select
            label="Blocked?"
            value={this.state.blocked}
            onChange={this.handleInputChange}
            name="blocked"
            SelectProps={{
              native: true
            }}>
            <option value=""></option>
            <option value="yes">Yes</option>
            <option value="no">No</option>
          </TextField>
          <Button
              style={{marginLeft: "10px"}}
              variant="contained"
              onClick={this.handleSubmit}>
            Filter
          </Button>
          <Button
              style={{marginLeft: "10px"}}
              variant="contained"
              onClick={this.handleClear}>
            Clear
          </Button>
        </Grid>
      </form>
    );
  }
}

export default(PalletTableFilter);
