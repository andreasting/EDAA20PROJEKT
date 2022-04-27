import React, { Component } from 'react';
import axios from 'axios';

import { withStyles } from '@material-ui/core/styles';
import Paper from '@material-ui/core/Paper';
import Typography from '@material-ui/core/Typography';

import './App.css';
import PalletProduction from './PalletProduction';
import PalletTable from './PalletTable'
import ResetDatabase from './ResetDatabase';

class App extends Component {
  state = {
    cookies: [],
    pallets: [],
    palletsFilter: {},
    disconnected: false,
    cookieStatus: "",
    palletStatus: ""
  }

  componentDidMount() {
    this.refresh();
  }

  refresh = () => {
    this.fetchCookies();
    this.fetchPallets(this.state.palletsFilter);
  }

  fetchCookies = () => {
    axios.get("cookies")
      .then(res => {
        if (res.data && res.data.cookies) {
          this.setState({ cookies: res.data.cookies, cookieStatus: (!res.data.cookies || res.data.cookies.length === 0) ? "Cookie request success, but no cookies returned from API": ""});
        } else {
          this.setState({ cookieStatus: "Couldn't fetch cookies from REST server"});
        }
      })
      .catch(error => {
        console.log(error);
        this.setState({disconnected: true})
      });
  }

  fetchPallets = (filter) => {
    // Only apply filters that are non-empty
    let params = {}
    Object.keys(filter)
      .filter(k => filter[k].length > 0)
      .forEach(k => params[k] = filter[k])

    axios.get("pallets", {params: params})
      .then(res => {
        console.log(res);
        if (res.data && res.data.pallets) {
          this.setState({ pallets: res.data.pallets, palletStatus: (!res.data.pallets || res.data.pallets.length === 0) ? "Pallet request success, but no pallets returned from API.": "" });
        } else {
          this.setState({ palletStatus: "Couldn't fetch pallets from REST server"});
        }
      })
      .catch(error => {
        console.log(error);
        this.setState({disconnected: true})
      });
  }

  setStatus = (message) => {
    this.setState({status: message});
  }

  setPalletsFilter = (filter) => {
    this.setState({palletsFilter: filter});
    this.fetchPallets(filter);
  }

  mainArea = () => (
    <div>
      <PalletProduction
        cookies={this.state.cookies}
        refresh={this.refresh}
        setStatus={this.setStatus} />
      <PalletTable
        cookies={this.state.cookies}
        pallets={this.state.pallets}
        palletsFilter={this.setPalletsFilter} />
      <ResetDatabase
        refresh={this.refresh}
        setStatus={this.setStatus} />
    </div>
  )

  render() {
    let statusMessage = [this.state.cookieStatus, this.state.palletStatus].join(" - ");
    if (this.state.disconnected) {
      statusMessage = (
        <b>Disconnected. Couldn't connect to REST server ({axios.defaults.baseURL})</b>
      )
    }

    let main = null;
    if (!this.state.disconnected) {
      main = this.mainArea();
    }

    return (
      <div className="App">
        <Typography variant="h3" gutterBottom>
          Krusty
        </Typography>

        <Paper className={this.props.classes.StatusPaper}>
          <div>
            <b>Status:</b> {statusMessage}
          </div>
        </Paper>

        {main}
      </div>
    );
  }

}

const styles = theme => ({
  StatusPaper: {
    padding: "10px",
    marginBottom: "15px"
  }
});

export default withStyles(styles)(App);
