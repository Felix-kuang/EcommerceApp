const Footer = () => {
  return (
    <footer className="bg-gray-100 text-center p-4 mt-10 dark:bg-gray-800">
      {" "}
      <p className="text-sm text-gray-500 ">
        &copy; {new Date().getFullYear()} E-Commerce. All rights reserved.
      </p>
    </footer>
  );
};

export default Footer;