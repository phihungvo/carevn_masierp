import React from 'react';
import { Spinner } from 'reactstrap';

const TableLoading = () => {
  return (
    <tbody className="loading-spinner container">
      <tr>
        <td>
          <div className="loading-spinner overlay" />
          <Spinner color="primary" className="loading-spinner--spin" />
        </td>
      </tr>
    </tbody>
  );
};

export default TableLoading;
