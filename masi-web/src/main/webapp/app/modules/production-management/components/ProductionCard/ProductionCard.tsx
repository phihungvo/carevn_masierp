import Flex from 'app/components/flex/flex';
import { ComponentProps } from 'react';
import { NavLink } from 'react-router-dom';
import { IconCheck, IconWarning } from '../icons';
import ProductionSummary from '../ProductionSummary/ProductionSummary';
import './ProductionCard.scss';

type Props = ComponentProps<typeof ProductionSummary> & {
  buttons: {
    content: string;
    hash: string;
    onClick: () => void;
    complete?: boolean;
    warning?: boolean;
    allowClick?: boolean;
  }[];
  children: React.ReactNode;
};

const ProductionCard = (props: Props) => {
  const { headerTitle, summaries, buttons, children } = props;

  const hash = window.location.hash.replace('#', '');

  const checkActive = (key: string) => {
    if (hash === key) return 'production-card__btn--active';
    if (key === 'dashboard' && hash === '')
      return 'production-card__btn--active';
    return '';
  };

  const checkAllow = (button: any) => {
    return (
      button.allowClick ||
      button.warning ||
      button.complete ||
      button.hash === 'dashboard'
    );
  };

  return (
    <Flex direction="column" rowGap={20}>
      <ProductionSummary headerTitle={headerTitle} summaries={summaries} />
      <section className="production-card__body">
        <div className="production-card__btns">
          {buttons.map((button, index) => (
            <NavLink
              to={'#' + button.hash}
              key={`production-card-button-${index}`}
              className={`production-card__btn ${checkActive(button.hash)} ${
                checkAllow(button) ? 'production-card__btn--allow' : ''
              }`}
              onClick={e => {
                if (checkAllow(button)) button.onClick();
                else e.preventDefault();
              }}
            >
              <span>{button.content}</span>
              {button.complete && IconCheck()} {button.warning && IconWarning()}
            </NavLink>
          ))}
        </div>
        <div className="production-card__content">{children}</div>
      </section>
    </Flex>
  );
};

export default ProductionCard;
