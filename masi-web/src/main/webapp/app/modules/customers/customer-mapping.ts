import { CUSTOMER_STATUS } from 'app/shared/model/enumerations/customer.model';

const mapCustomerStatusText = (status: CUSTOMER_STATUS): string => {
  switch (status) {
    case CUSTOMER_STATUS.ENABLED:
      return 'Hoạt động';
    case CUSTOMER_STATUS.DISABLED:
      return 'Vô hiệu';
    default:
      return '';
  }
};

export default {
  mapCustomerStatusText,
};
