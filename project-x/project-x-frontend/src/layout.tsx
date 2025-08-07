import { Outlet } from "react-router";
import AppHeader from "./components/layout/app.header";
import AppFooter from "./components/layout/app.footer";
import { App } from "antd";

const AppLayout = () => {
  return (
    <App>
      <AppHeader />
      <Outlet/>
      <AppFooter />
    </App>
  );
};

export default AppLayout;
