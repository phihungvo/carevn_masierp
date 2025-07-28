import 'app/config/dayjs';
import 'react-toastify/dist/ReactToastify.css';
import './app.scss';

import { useEffect } from 'react';
import { BrowserRouter } from 'react-router-dom';

import { ConfigProvider } from 'antd';
import { useAppDispatch, useAppSelector } from 'app/config/store';
import AppRoutes from 'app/routes';
import ErrorBoundary from 'app/shared/error/error-boundary';
import { getProfile } from 'app/shared/reducers/application-profile';
import { getSession } from 'app/shared/reducers/authentication';
import Modal from './components/modal/modal';
import './config/custom-locale';
import useAccountApp from './hooks/use-account-app';
import useEmployee from './hooks/use-employee';
import useModalRedux from './hooks/use-modal-redux';
import { PrintComponents } from './print-components';
import { Account } from './shared/model/account.model';

const {
  useGetProfileDefault
} = useEmployee

const baseHref = document
  .querySelector('base')
  .getAttribute('href')
  .replace(/\/$/, '');

export const App = () => {
  const dispatch = useAppDispatch();
  const { modalState } = useModalRedux();

  // Get current profile user
  useGetProfileDefault()

  useEffect(() => {
    dispatch(getSession());
    dispatch(getProfile());
  }, []);

  return (
    <ConfigProvider theme={{
      token: {
        colorPrimary: "#4479c5",
        colorInfo: "#4479c5",
        fontFamily: 'Inter, sans-serif'
      },
      components: {
        Button: {
          defaultColor: "#4072D0",
          colorBgContainerDisabled: "#F2F4F7",
          colorTextDisabled: "rgb(199, 210, 227)",
          defaultBorderColor: "#98A2B3",
        },
        Input: {
          colorBorder: "#D0D5DD",
          colorTextPlaceholder: "#667085",
          colorBgContainerDisabled: "#D0D5DD", 
        },
        InputNumber: {
          colorBorder: "#D0D5DD",
          colorTextPlaceholder: "#667085"
        },
        Select: {
          colorBorder: "#D0D5DD",
          colorTextPlaceholder: "#667085",
          colorBgContainerDisabled: "#D0D5DD",
        },
        DatePicker: {
          colorBorder: "#D0D5DD",
          colorTextPlaceholder: "#667085",
          colorBgContainerDisabled: "#D0D5DD",
        },
        Form: {
          colorBorder: "#98A2B3",
          itemMarginBottom: 0,
        },
      },
    }}>
      <BrowserRouter basename={baseHref}>
        <ErrorBoundary>
          <AppRoutes />
          <PrintComponents />
          <Modal
            isOpen={modalState?.isOpen}
            title={modalState?.title}
            titleHeader={modalState?.title}
            okText={modalState?.okText}
            ok={!!modalState?.okText}
            cancelText={modalState?.cancelText}
            cancel={!!modalState?.cancelText}
            onOk={modalState?.onOK}
            onCancel={modalState?.onCancel}
            toggle={modalState?.onCancel}
          >
            {modalState?.content}
          </Modal>
        </ErrorBoundary>
      </BrowserRouter>
    </ConfigProvider>

  );
};

export default App;
