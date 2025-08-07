import { Outlet } from "react-router";
import AppHeader from "./components/layout/app.header";
import AppFooter from "./components/layout/app.footer";

const AppLayout = () => {
  return (
    <>
      <AppHeader />
      <Outlet/>
      <AppFooter />
    </>
  );
};

export default AppLayout;
