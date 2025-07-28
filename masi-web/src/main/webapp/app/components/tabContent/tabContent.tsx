import React from 'react';
import { CardProps, TabPane } from 'reactstrap';

interface ITabs extends CardProps {
  children: React.ReactNode;
  id: string;
}

const TabContentItem = (props: ITabs) => {
  const { header, children, className, classNameHeader, id, ...rest } = props;

  return <TabPane tabId={id}>{children}</TabPane>;
};

export default TabContentItem;
