import { useAppDispatch, useAppSelector } from 'app/config/store';
import MenuItem from 'app/shared/layout/menus/menu-item';
import { logout } from 'app/shared/reducers/authentication';
import './account.scss';
import { NavDropdown } from './menu-components';

const accountMenuItemsAuthenticated = account => {
  const dispatch = useAppDispatch();

  return (
    <>
      <div className="dropdown-item-info">
        {avatar()} {(account?.lastName || '') + ' ' + account?.firstName}
      </div>
      <MenuItem icon="wrench" to="/account/settings" data-cy="settings">
        Cài đặt
      </MenuItem>
      <MenuItem icon="lock" to="/account/password" data-cy="passwordItem">
        Đổi mật khẩu
      </MenuItem>
      <MenuItem
        onClick={() => dispatch(logout())}
        icon="sign-out-alt"
        to="/login"
        data-cy="logout"
      >
        Đăng xuất
      </MenuItem>
    </>
  );
};

const accountMenuItems = () => (
  <>
    <MenuItem id="login-item" icon="sign-in-alt" to="/login" data-cy="login">
      Đăng nhập
    </MenuItem>
    {/* <MenuItem icon="user-plus" to="/account/register" data-cy="register">
      Register
    </MenuItem> */}
  </>
);

//TODO: Fixed for now. Waiting for fully implementation of display real avatar..
const avatar = () => (
  <div className={'account-avatar'}>
    <img
      src="../../../../content/images/vuesax/bulk/default-avatar.svg"
      alt="avatar"
    />
  </div>
);

export const AccountMenu = ({ isAuthenticated = false }) => {
  const account = useAppSelector(state => state.authentication.account);
  return (
    <NavDropdown
      img={avatar()}
      name={(account?.lastName || '') + ' ' + account?.firstName}
      id="account-menu"
      data-cy="accountMenu"
    >
      {isAuthenticated && accountMenuItemsAuthenticated(account)}
      {!isAuthenticated && accountMenuItems()}
    </NavDropdown>
  );
};

export default AccountMenu;
