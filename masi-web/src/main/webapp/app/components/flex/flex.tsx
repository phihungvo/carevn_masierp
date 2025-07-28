import React from 'react';

interface IFlex extends React.HTMLAttributes<HTMLDivElement> {
  direction?: 'row' | 'column' | 'row-reverse' | 'column-reverse';
  align?: 'start' | 'center' | 'end' | 'stretch';
  justify?: 'start' | 'center' | 'end' | 'space-between' | 'space-around' | 'space-evenly';
  gap?: number;
  children: React.ReactNode;
  alignSelf?: 'start' | 'center' | 'end' | 'stretch';
  flexWrap?: 'wrap';
  flexBasis?: string;
  columnGap?: number;
  rowGap?: number;
}

const Flex = (props: IFlex) => {
  const {
    direction = 'row', align, justify, gap, columnGap, rowGap,
    children, style, alignSelf, flexWrap, flexBasis, ...rest
  } = props;
  return (
    <div
      {...rest}
      style={{
        display: 'flex',
        flexWrap,
        flexDirection: direction,
        alignItems: align,
        justifyContent: justify,
        alignSelf,
        gap: `${gap}px`,
        flexBasis,
        columnGap: `${columnGap}px`,
        rowGap: `${rowGap}px`,
        ...style,
      }}
    >
      {children}
    </div>
  );
};

export default Flex;
