import { convertCurrency } from 'app/shared/util/format';
import React from 'react';
import Flex from '../flex/flex';
import { Typography } from '../typography/typography';
import './TotalMoney.scss';

type Props = {
  total: number;
};

const TotalMoney = ({ total }: Props) => {
  return (
    <Flex className="total-money" justify="center" gap={24}>
      <Typography level={5}>Tổng tiền:</Typography>
      <div className="total-money__content">
        {convertCurrency(total, false)}
      </div>
      <Typography level={5}>VND</Typography>
    </Flex>
  );
};

export default TotalMoney;
