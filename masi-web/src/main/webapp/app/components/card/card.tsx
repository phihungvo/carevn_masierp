import './card.scss';
import React from 'react';
import { Card as CardStrap, CardBody, CardHeader, CardProps } from 'reactstrap';

interface ICard extends CardProps {
  children: React.ReactNode;
  header?: React.ReactNode;
}

const Card = (props: ICard) => {
  const { header, children, className, classNameHeader, ...rest } = props;

  return (
    <div {...rest}>
      {header && <CardHeader className={classNameHeader}>{header}</CardHeader>}
      <CardBody className={className}>{children}</CardBody>
    </div>
  );
};

export default Card;
