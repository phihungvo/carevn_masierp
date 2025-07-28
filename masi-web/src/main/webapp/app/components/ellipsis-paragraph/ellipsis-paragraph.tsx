import React from 'react';

import './ellipsis-paragraph.scss';

interface IEllipsisParagraphProps {
  text: string | React.ReactNode;
  width?: number;
  id?: string;
  className?: string;
  onClick?: () => void;
}

const EllipsisParagraph = (props: IEllipsisParagraphProps) => {
  const { text, width = 200, id, className = '', onClick } = props;
  return (
    <p
      className={`ellipsis-paragraph ${className}`}
      id={id}
      style={{ width: width }}
      onClick={() => {
        if (onClick) onClick();
      }}
    >
      {text}
    </p>
  );
};

export default EllipsisParagraph;
