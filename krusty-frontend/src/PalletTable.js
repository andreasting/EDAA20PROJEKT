import React from 'react';
import PropTypes from 'prop-types';

import PalletTableFilter from './PalletTableFilter'

import { withStyles } from '@material-ui/core/styles';
import Table from '@material-ui/core/Table';
import TableBody from '@material-ui/core/TableBody';
import TableCell from '@material-ui/core/TableCell';
import TableHead from '@material-ui/core/TableHead';
import TableRow from '@material-ui/core/TableRow';
import Paper from '@material-ui/core/Paper';
import Typography from '@material-ui/core/Typography';

const styles = theme => ({
  root: {
    width: '100%',
    overflowX: 'auto',
  },
  table: {
    minWidth: 400,
  },
  header: {
    marginLeft: '10px',
    marginTop: '10px'
  }
});

function PalletTable(props) {
  const { classes } = props;

  return (
    <Paper className={classes.root}>
      <Typography className={classes.header} color="textSecondary" variant="button" gutterBottom>
        Produced Pallets
      </Typography>
      <PalletTableFilter
        cookies={props.cookies}
        palletsFilter={props.palletsFilter} />
      <Table className={classes.table}>
        <TableHead>
          <TableRow>
            <TableCell>Name</TableCell>
            <TableCell>Production date</TableCell>
            <TableCell>Customer</TableCell>
            <TableCell>Blocked</TableCell>
          </TableRow>
        </TableHead>
        <TableBody>
          {props.pallets.map(p => {
            const customer = !p.customer || p.customer === 'null' ? '' : p.customer;
            return (
              <TableRow key={p.id}>
                <TableCell component="th" scope="row">
                  {p.cookie}
                </TableCell>
                <TableCell>{p.production_date}</TableCell>
                <TableCell>{customer}</TableCell>
                <TableCell>{p.blocked}</TableCell>
              </TableRow>
            );
          })}
        </TableBody>
      </Table>
    </Paper>
  );
}

PalletTable.propTypes = {
  classes: PropTypes.object.isRequired,
};

export default withStyles(styles)(PalletTable);
