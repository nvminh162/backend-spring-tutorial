import { Table } from "antd";
import axios from "axios";
import { useEffect } from "react";

const UserPage = () => {

  useEffect(() => {
    axios.get("http://localhost:8080/users")
  }, [])

  const dataSource = [
    {
      key: "1",
      name: "Mike",
      age: 32,
      address: "10 Downing Street",
    },
    {
      key: "2",
      name: "John",
      age: 42,
      address: "10 Downing Street",
    },
  ];

  const columns = [
    {
      title: "Name",
      dataIndex: "name",
      key: "name",
    },
    {
      title: "Age",
      dataIndex: "age",
      key: "age",
    },
    {
      title: "Address",
      dataIndex: "address",
      key: "address",
    },
  ];

  return (
    <div style={{ padding: 10 }}>
      <Table bordered dataSource={dataSource} columns={columns} />
    </div>
  );
};

export default UserPage;
