import React from 'react';
import { CardBody, CardHeader, Card as CardStrap, Nav, NavItem, NavLink, TabContent, TabContentProps } from 'reactstrap';
import './tabs.scss';

interface ITabs extends TabContentProps {
  children: React.ReactNode;
  header?: { id: string; title: string }[];
}

const Tabs = (props: ITabs) => {
  const { header, children, className, classNameHeader, ...rest } = props;

  const [activeTab, setActiveTab] = React.useState(header[0].id);

  return (
    <CardStrap {...rest}>
      {header && (
        <CardHeader className={classNameHeader}>
          <Nav tabs fill>
            {header.map(x => (
              <NavItem key={x.id}>
                <NavLink active={activeTab === x.id} onClick={() => setActiveTab(x.id)}>
                  {x.title}
                </NavLink>
              </NavItem>
            ))}
          </Nav>
        </CardHeader>
      )}
      <CardBody className={className}>
        <TabContent activeTab={activeTab}>{children}</TabContent>
      </CardBody>
    </CardStrap>
  );
};

export default Tabs;
