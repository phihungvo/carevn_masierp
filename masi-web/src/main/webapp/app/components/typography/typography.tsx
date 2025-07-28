import { ColorType } from 'app/shared/model/enumerations/color.model';
import './typography.scss';
import React from 'react';

interface ITypography extends React.HTMLAttributes<HTMLHeadingElement> {
  level: 1 | 2 | 3 | 4 | 5 | 6 | 'paragraph' | 'text';
  children: React.ReactNode;
  color?: ColorType;
  className?: string;
}

export const Typography = (props: ITypography) => {
  const { level, children, color = 'primary', className, ...rest } = props;

  const mergeClassName = `${color ? color : ''} ${className ? className : ''}`.trim();

  switch (level) {
    case 1:
      // FONT SIZE 44PX AND FONT WEIGHT 700
      return (
        <h1 {...rest} className={mergeClassName}>
          {children}
        </h1>
      );
    case 2:
      return (
        <h2 {...rest} className={mergeClassName}>
          {children}
        </h2>
      );
    case 3:
      // FONT SIZE 36PX AND FONT WEIGHT 700
      return (
        <h3 {...rest} className={mergeClassName}>
          {children}
        </h3>
      );
    case 4:
      // FONT SIZE 24PX AND FONT WEIGHT 400
      return (
        <h4 {...rest} className={mergeClassName}>
          {children}
        </h4>
      );
    case 5:
      // FONT SIZE 24PX AND FONT WEIGHT 400
      return (
        <h5 {...rest} className={mergeClassName}>
          {children}
        </h5>
      );
    case 6:
      return (
        <h6 {...rest} className={mergeClassName}>
          {children}
        </h6>
      );
    case 'paragraph':
      return (
        <p {...rest} className={mergeClassName}>
          {children}
        </p>
      );
    case 'text':
      return (
        <span {...rest} className={mergeClassName}>
          {children}
        </span>
      );
    default:
      return (
        <p {...rest} className={mergeClassName}>
          {children}
        </p>
      );
  }
};
