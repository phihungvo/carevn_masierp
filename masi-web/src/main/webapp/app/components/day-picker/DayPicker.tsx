import {
  autoUpdate,
  flip,
  FloatingFocusManager,
  offset,
  shift,
  useClick,
  useDismiss,
  useFloating,
  useInteractions,
} from '@floating-ui/react';
import { ComponentProps, useState } from 'react';
import {
  DayPicker as DayPickerLib,
  Modifiers,
  OnSelectHandler,
} from 'react-day-picker';
import { vi } from 'react-day-picker/locale';
import './DayPicker.scss';
import Input from '../input/input';
import ReactInputMask from 'react-input-mask';
import { DATE_FORMAT } from 'app/constants/common';

type Props = ComponentProps<'input'> & {
  label?: string;
  icon?: boolean;
  children?: React.ReactNode;
  value?: Date[] | Date;
  closeExportCalendar: () => void;
  formState?: any;
  range?: boolean;
  format?: string;
};

const DayPicker = (props: Props) => {
  const {
    label,
    id,
    name,
    placeholder,
    icon = true,
    className,
    disabled,
    children,
    onBlur,
    value,
    format = DATE_FORMAT.DATE,
    closeExportCalendar,
    formState,
    range,
    onFocus,
    ...rest
  } = props;

  const [isOpen, setIsOpen] = useState(false);
  const [date, setDate] = useState<Date>();
  const [dateString, setDateString] = useState<string>('');

  const { refs, floatingStyles, context } = useFloating({
    open: isOpen,
    onOpenChange: setIsOpen,
    middleware: [offset(15), flip(), shift()],
    whileElementsMounted: autoUpdate,
  });

  const click = useClick(context, {
    toggle: false,
  });
  const dismiss = useDismiss(context);

  const { getReferenceProps, getFloatingProps } = useInteractions([
    click,
    dismiss,
  ]);

  const onDateChange: OnSelectHandler<Date> = (
    selected: Date,
    triggerDate: Date,
    modifiers: Modifiers,
    e: React.MouseEvent | React.KeyboardEvent,
  ) => {
    console.log('selected', selected);
    console.log('triggerDate', triggerDate);
    console.log('modifiers', modifiers);
    console.log('e', e);
  };

  const onDateStringChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    let value = e.target.value;
    setDateString(value);
  };

  return (
    <>
      <div ref={refs.setReference} {...getReferenceProps()}>
        <ReactInputMask
          mask="dd/mm/yyyy"
          value={dateString}
          onChange={onDateStringChange}
        />
      </div>
      {isOpen && (
        <FloatingFocusManager context={context} modal={false} disabled>
          <div
            ref={refs.setFloating}
            style={{ ...floatingStyles, zIndex: 1000 }}
            {...getFloatingProps()}
          >
            <DayPickerLib
              mode="single"
              selected={date}
              onSelect={onDateChange}
              showOutsideDays={false}
              className="day-picker"
              locale={vi}
            />
          </div>
        </FloatingFocusManager>
      )}
    </>
  );
};

export default DayPicker;
